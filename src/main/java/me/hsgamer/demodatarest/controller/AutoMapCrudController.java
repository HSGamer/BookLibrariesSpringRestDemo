package me.hsgamer.demodatarest.controller;

import org.modelmapper.ModelMapper;
import org.springframework.data.repository.CrudRepository;

public abstract class AutoMapCrudController<T, ID, RP extends CrudRepository<T, ID>, REQ, RES> extends CrudController<T, ID, RP, REQ, RES> {
    protected final ModelMapper modelMapper;

    protected AutoMapCrudController(RP repository, ModelMapper modelMapper) {
        super(repository);
        this.modelMapper = modelMapper;
    }

    protected abstract Class<RES> getResponseClass();

    protected abstract void afterMap(T t, REQ req);

    @Override
    protected RES toResponse(T t) {
        return modelMapper.map(t, getResponseClass());
    }

    @Override
    protected void updateEntity(T t, REQ req) {
        modelMapper.map(req, t);
        afterMap(t, req);
    }
}
