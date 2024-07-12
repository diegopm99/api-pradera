package com.pradera.praderaback.service;

import com.pradera.praderaback.dto.UsuarioDTO;
import com.pradera.praderaback.model.UsuarioModel;
import com.pradera.praderaback.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder cb;
    private CriteriaQuery<UsuarioModel> cq;
    private Root<UsuarioModel> root;
    private CriteriaQuery<Long> cqCont;
    private Root<UsuarioModel> rootCont;

    public void initCb(){
        cb = em.getCriteriaBuilder();
        cq = cb.createQuery(UsuarioModel.class);
        root = cq.from(UsuarioModel.class);
        cqCont = cb.createQuery(Long.class);
        rootCont = cqCont.from(UsuarioModel.class);
    }

    public TypedQuery<UsuarioModel> filtrado(UsuarioDTO filtro) {
        initCb();
        cqCont.select(cb.count(rootCont));
        Predicate[] predicatesArray;
        var predicates = new ArrayList<Predicate>();

        if (filtro.getUsername() != null) {
            predicates.add(cb.equal(root.get("username"),filtro.getUsername()));
        }
        predicatesArray = predicates.toArray(new Predicate[0]);
        cq.where(predicatesArray);
        cqCont.where(predicatesArray);
        cq.select(root).distinct(true);
        return em.createQuery(cq);
    }

    public Page<UsuarioDTO> bandeja(UsuarioDTO filtro, Integer page, Integer size ){
        var result = this.filtrado(filtro);
        var resultCont = em.createQuery(cqCont);
        Long all = resultCont.getSingleResult();
        result = result.setFirstResult(page);
        result = result.setMaxResults(size);
        var resultList = result.getResultList();
        em.close();
        List<UsuarioDTO> response = resultList.stream().map(x ->
                modelMapper.map(x, UsuarioDTO.class)
        ).collect(Collectors.toList());
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(response, pageable, all);
    }

    public List<UsuarioDTO> listar(){
        List<UsuarioModel> data = repository.findAll();
        List<UsuarioDTO> listadto = new ArrayList<>();
        for (UsuarioModel usuario : data){
            UsuarioDTO dto = modelMapper.map(usuario, UsuarioDTO.class);
            listadto.add(dto);
        }
        return listadto;
    }

    public UsuarioDTO obtener(Long id){
        return modelMapper.map(repository.findById(id).orElse(null), UsuarioDTO.class);
    }

    private void registrar(UsuarioDTO usuarioDTO){
        usuarioDTO.setPassword(new BCryptPasswordEncoder().encode(usuarioDTO.getPassword()));
        repository.save(modelMapper.map(usuarioDTO, UsuarioModel.class));
    }

    private void actualizar(UsuarioDTO usuarioDTO){
        UsuarioModel usuarioModel = repository.findById(usuarioDTO.getId()).orElse(null);
        if(!usuarioModel.getPassword().equals(usuarioDTO.getPassword())){
            usuarioDTO.setPassword(new BCryptPasswordEncoder().encode(usuarioDTO.getPassword()));
        }
        repository.save(modelMapper.map(usuarioDTO, UsuarioModel.class));
    }

    public void guardar(UsuarioDTO usuarioDTO){
        if(usuarioDTO.getId() == null){
            registrar(usuarioDTO);
        } else{
            actualizar(usuarioDTO);
        }
    }

    public void eliminar(Long id){
        repository.deleteById(id);
    }

    public boolean existsByUsername(String username){
        return repository.existsByUsername(username);
    }

    public boolean avaibleUsername(UsuarioDTO usuarioDTO){
        UsuarioModel usuarioModel = repository.findUserByUsername(usuarioDTO.getUsername(), usuarioDTO.getId()).orElse(null);
        return usuarioModel == null;
    }

    public UsuarioDTO findByUsername(String username){
        return modelMapper.map(repository.findByUsername(username).orElse(null), UsuarioDTO.class);
    }
}
