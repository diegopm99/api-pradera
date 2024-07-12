package com.pradera.praderaback.service;

import com.pradera.praderaback.dto.TrabajadorDTO;
import com.pradera.praderaback.model.TrabajadorModel;
import com.pradera.praderaback.repository.TrabajadorRepository;
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
public class TrabajadorService {

    @Autowired
    private TrabajadorRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder cb;
    private CriteriaQuery<TrabajadorModel> cq;
    private Root<TrabajadorModel> root;
    private CriteriaQuery<Long> cqCont;
    private Root<TrabajadorModel> rootCont;

    public void initCb(){
        cb = em.getCriteriaBuilder();
        cq = cb.createQuery(TrabajadorModel.class);
        root = cq.from(TrabajadorModel.class);
        cqCont = cb.createQuery(Long.class);
        rootCont = cqCont.from(TrabajadorModel.class);
    }

    public TypedQuery<TrabajadorModel> filtrado(TrabajadorDTO filtro) {
        initCb();
        cqCont.select(cb.count(rootCont));
        Predicate[] predicatesArray;
        var predicates = new ArrayList<Predicate>();
        if (filtro.getApellidop() != null) {
            predicates.add(cb.equal(root.get("apellidop"),filtro.getApellidop()));
        }
        predicatesArray = predicates.toArray(new Predicate[0]);
        cq.where(predicatesArray);
        cqCont.where(predicatesArray);
        cq.select(root).distinct(true);
        return em.createQuery(cq);
    }

    public Page<TrabajadorDTO> bandeja(TrabajadorDTO filtro, Integer page, Integer size ){
        var result = this.filtrado(filtro);
        var resultCont = em.createQuery(cqCont);
        Long all = resultCont.getSingleResult();
        result = result.setFirstResult(page);
        result = result.setMaxResults(size);
        var resultList = result.getResultList();
        em.close();
        List<TrabajadorDTO> response = new ArrayList<>();
        for(TrabajadorModel trabajador : resultList){
            TrabajadorDTO dto = new TrabajadorDTO();
            dto.setId(trabajador.getId());
            dto.setNombres(trabajador.getNombres());
            dto.setApellidop(trabajador.getApellidop());
            dto.setApellidom(trabajador.getApellidom());
            dto.setNombreCompleto(trabajador.nombreCompleto());
            dto.setDni(trabajador.getDni());
            response.add(dto);
        }
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(response, pageable, all);
    }

    public TrabajadorDTO obtener(Long id) {
        TrabajadorModel trabajador = repository.findById(id).orElse(null);
        return modelMapper.map(trabajador, TrabajadorDTO.class);
    }

    public List<TrabajadorDTO> listar() {
        List<TrabajadorDTO> listadto = new ArrayList<>();
        List<TrabajadorModel> listamodelo = repository.findAll();
        for (TrabajadorModel trabajador : listamodelo
        ) {
            TrabajadorDTO dto = new TrabajadorDTO();
            dto.setId(trabajador.getId());
            dto.setNombres(trabajador.getNombres());
            dto.setApellidop(trabajador.getApellidop());
            dto.setApellidom(trabajador.getApellidom());
            dto.setNombreCompleto(trabajador.nombreCompleto());
            dto.setDni(trabajador.getDni());
            listadto.add(dto);
        }
        return listadto;
    }

    public void guardar(TrabajadorDTO dto) {
        TrabajadorModel trabajador = modelMapper.map(dto, TrabajadorModel.class);
        repository.save(trabajador);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

}
