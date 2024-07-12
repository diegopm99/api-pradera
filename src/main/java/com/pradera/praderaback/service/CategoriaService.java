package com.pradera.praderaback.service;

import com.pradera.praderaback.dto.CategoriaDTO;
import com.pradera.praderaback.dto.ProductoDTO;
import com.pradera.praderaback.model.CategoriaModel;
import com.pradera.praderaback.model.ProductoModel;
import com.pradera.praderaback.repository.CategoriaRepository;
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
public class CategoriaService {

    @Autowired
    private CategoriaRepository repositorio;
    @Autowired
    private ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder cb;
    private CriteriaQuery<CategoriaModel> cq;
    private Root<CategoriaModel> root;
    private CriteriaQuery<Long> cqCont;
    private Root<CategoriaModel> rootCont;


    public void initCb(){
        cb = em.getCriteriaBuilder();
        cq = cb.createQuery(CategoriaModel.class);
        root = cq.from(CategoriaModel.class);
        cqCont = cb.createQuery(Long.class);
        rootCont = cqCont.from(CategoriaModel.class);
    }
    public TypedQuery<CategoriaModel> filtrado(CategoriaDTO filtro) {
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
    public Page<CategoriaDTO> bandeja(CategoriaDTO filtro, Integer page, Integer size ){
        var result = this.filtrado(filtro);
        var resultCont = em.createQuery(cqCont);
        Long all = resultCont.getSingleResult();
        result = result.setFirstResult(page);
        result = result.setMaxResults(size);
        var resultList = result.getResultList();
        em.close();
        List<CategoriaDTO> response = resultList.stream().map(x ->
                modelMapper.map(x, CategoriaDTO.class)
        ).collect(Collectors.toList());
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(response, pageable, all);
    }


    public void guardar(CategoriaDTO dto) {
        CategoriaModel categoria = new CategoriaModel();
        categoria.setId(dto.getId());

        if (dto.getId() != null){
            categoria = repositorio.getById(dto.getId());
            categoria.setNombre(dto.getNombre());

        }else{
            categoria.setId(dto.getId());
            categoria.setNombre(dto.getNombre());

        }
        repositorio.save(categoria);
    }

    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }

    public List<CategoriaDTO> listar() {
        List<CategoriaDTO> listadto = new ArrayList<>();
        List<CategoriaModel> listamodelo= repositorio.findAll();
        for (CategoriaModel modelo:listamodelo
             ) {
            CategoriaDTO dto = new CategoriaDTO();
            dto.setId(modelo.getId());
            dto.setNombre(modelo.getNombre());
            listadto.add(dto);
        }
        return listadto;
    }

    public CategoriaDTO obtener(Long id) {
        CategoriaModel categoria = repositorio.findById(id).orElse(null);
        CategoriaDTO dto = null;
        if(categoria != null){
            dto = new CategoriaDTO();
            dto.setId(categoria.getId());
            dto.setNombre(categoria.getNombre());
        }
        return dto;
    }
}
