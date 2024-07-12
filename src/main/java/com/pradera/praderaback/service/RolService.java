package com.pradera.praderaback.service;

import com.pradera.praderaback.dto.RolDTO;
import com.pradera.praderaback.model.RolModel;
import com.pradera.praderaback.repository.RolRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class RolService {

    @Autowired
    private RolRepository repository;
    @Autowired
    private ModelMapper modelMapper;


    public List<RolDTO> listar() {
        List<RolDTO> listadto = new ArrayList<>();
        List<RolModel> listamodelo = repository.findAll();
        for (RolModel rol : listamodelo
        ) {
            RolDTO dto = modelMapper.map(rol, RolDTO.class);
            listadto.add(dto);
        }
        return listadto;
    }

}
