package me.hsgamer.demodatarest.controller;

import org.modelmapper.ModelMapper;
import org.springframework.data.repository.CrudRepository;

public abstract class AutoMapCrudController<T, ID, RP extends CrudRepository<T, ID>, REQ, RES> implements CrudController<T, ID, RP, REQ, RES> {
    protected final ModelMapper modelMapper;

    protected AutoMapCrudController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    protected abstract Class<RES> getResponseClass();

    protected abstract Class<REQ> getRequestClass();

    protected abstract void mapToEntity(T t, REQ req);

    protected abstract void mapToRequest(T t, REQ req);

    @Override
    public RES toResponse(T t) {
        return modelMapper.map(t, getResponseClass());
    }

    @Override
    public REQ toRequest(T t) {
        REQ req = modelMapper.map(t, getRequestClass());
        mapToRequest(t, req);
        return req;
    }

    @Override
    public void updateEntity(T t, REQ req) {
        modelMapper.map(req, t);
        mapToEntity(t, req);
    }
}
