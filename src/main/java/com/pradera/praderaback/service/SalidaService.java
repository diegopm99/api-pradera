package com.pradera.praderaback.service;

import com.pradera.praderaback.dto.SalidaDTO;
import com.pradera.praderaback.model.IngresosModel;
import com.pradera.praderaback.model.ProductoModel;
import com.pradera.praderaback.model.SalidaModel;
import com.pradera.praderaback.model.TrabajadorModel;
import com.pradera.praderaback.repository.ProductoRepository;
import com.pradera.praderaback.repository.SalidaRepository;
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
public class SalidaService {

    @Autowired
    private SalidaRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder cb;
    private CriteriaQuery<SalidaModel> cq;
    private Root<SalidaModel> root;
    private CriteriaQuery<Long> cqCont;
    private Root<SalidaModel> rootCont;

    public void initCb(){
        cb = em.getCriteriaBuilder();
        cq = cb.createQuery(SalidaModel.class);
        root = cq.from(SalidaModel.class);
        cqCont = cb.createQuery(Long.class);
        rootCont = cqCont.from(SalidaModel.class);
    }

    public TypedQuery<SalidaModel> filtrado(SalidaDTO filtro) {
        initCb();
        cqCont.select(cb.count(rootCont));
        Predicate[] predicatesArray;
        var predicates = new ArrayList<Predicate>();
        if (filtro.getProductoNombre() != null) {
            predicates.add(cb.equal(root.get("producto").get("nombre"),filtro.getProductoNombre()));
        }
        predicatesArray = predicates.toArray(new Predicate[0]);
        cq.where(predicatesArray);
        cqCont.where(predicatesArray);
        cq.select(root).distinct(true);
        return  em.createQuery(cq);
    }

    public Page<SalidaDTO> bandeja(SalidaDTO filtro, Integer page, Integer size ){
        var result = this.filtrado(filtro);
        var resultCont = em.createQuery(cqCont);
        Long all = resultCont.getSingleResult();
        result = result.setFirstResult(page);
        result = result.setMaxResults(size);
        var resultList = result.getResultList();
        em.close();
        List<SalidaDTO> response = new ArrayList<>();
        for (SalidaModel data: resultList) {
            SalidaDTO salida = new SalidaDTO();
            salida.setId(data.getId());
            salida.setFecha(data.getFecha());
            salida.setCantidad(data.getCantidad());
            salida.setProductoId(data.getProducto().getId());
            salida.setProductoNombre(data.getProducto().getNombre());
            salida.setTrabajadorId(data.getTrabajador().getId());
            salida.setTrabajadorNombre(data.getTrabajador().nombreCompleto());
            response.add(salida);
        }
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(response, pageable, all);
    }

    public SalidaDTO obtener(Long id) {
        SalidaModel salida = repository.findById(id).orElse(null);
        return modelMapper.map(salida, SalidaDTO.class);
    }

    public List<SalidaDTO> listar() {
        List<SalidaDTO> listadto = new ArrayList<>();
        List<SalidaModel> listamodelo = repository.findAll();
        for (SalidaModel data: listamodelo) {
            SalidaDTO salida = new SalidaDTO();
            salida.setId(data.getId());
            salida.setFecha(data.getFecha());
            salida.setCantidad(data.getCantidad());
            salida.setProductoId(data.getProducto().getId());
            salida.setProductoNombre(data.getProducto().getNombre());
            salida.setTrabajadorId(data.getTrabajador().getId());
            salida.setTrabajadorNombre(data.getTrabajador().nombreCompleto());
            listadto.add(salida);
        }
        return listadto;
    }

    public void guardar(SalidaDTO dto) {
        SalidaModel salida = new SalidaModel();
        ProductoModel producto = new ProductoModel();
        TrabajadorModel trabajador = new TrabajadorModel();
        producto.setId(dto.getProductoId());
        trabajador.setId(dto.getTrabajadorId());
        salida.setId(dto.getId());
        salida.setProducto(producto);
        salida.setTrabajador(trabajador);
        salida.setCantidad(dto.getCantidad());
        repository.save(salida);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
