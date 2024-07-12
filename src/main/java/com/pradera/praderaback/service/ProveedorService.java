package com.pradera.praderaback.service;

import com.pradera.praderaback.dto.ProveedorDTO;
import com.pradera.praderaback.model.ProveedorModel;
import com.pradera.praderaback.repository.ProveedorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder cb;
    private CriteriaQuery<ProveedorModel> cq;
    private Root<ProveedorModel> root;
    private CriteriaQuery<Long> cqCont;
    private Root<ProveedorModel> rootCont;

    public void initCb(){
        cb = em.getCriteriaBuilder();
        cq = cb.createQuery(ProveedorModel.class);
        root = cq.from(ProveedorModel.class);
        cqCont = cb.createQuery(Long.class);
        rootCont = cqCont.from(ProveedorModel.class);
    }

    public TypedQuery<ProveedorModel> filtrado(ProveedorDTO filtro) {
        initCb();
        cqCont.select(cb.count(rootCont));
        Predicate[] predicatesArray;
        var predicates = new ArrayList<Predicate>();
        if (filtro.getNombre() != null) {
            predicates.add(cb.equal(root.get("nombre"),filtro.getNombre()));
        }
        predicatesArray = predicates.toArray(new Predicate[0]);
        cq.where(predicatesArray);
        cqCont.where(predicatesArray);
        cq.select(root).distinct(true);
        return  em.createQuery(cq);
    }

    public Page<ProveedorDTO> bandeja(ProveedorDTO filtro, Integer page, Integer size ){
        var result = this.filtrado(filtro);
        var resultCont = em.createQuery(cqCont);
        Long all = resultCont.getSingleResult();
        result = result.setFirstResult(page);
        result = result.setMaxResults(size);
        var resultList = result.getResultList();
        em.close();
        List<ProveedorDTO> response = resultList.stream().map(x ->
                modelMapper.map(x, ProveedorDTO.class)
        ).collect(Collectors.toList());
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(response, pageable, all);
    }

    public ProveedorDTO obtener(Long id) {
        ProveedorModel proveedor = repository.findById(id).orElse(null);
        return modelMapper.map(proveedor, ProveedorDTO.class);
    }

    public List<ProveedorDTO> listar() {
        List<ProveedorDTO> listadto = new ArrayList<>();
        List<ProveedorModel> listamodelo = repository.findAll();
        for (ProveedorModel proveedor : listamodelo
        ) {
            ProveedorDTO dto = modelMapper.map(proveedor, ProveedorDTO.class);
            listadto.add(dto);
        }
        return listadto;
    }

    public void guardar(ProveedorDTO dto) {
        ProveedorModel proveedor = modelMapper.map(dto, ProveedorModel.class);
        repository.save(proveedor);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

}
