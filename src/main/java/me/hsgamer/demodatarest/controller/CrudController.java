package me.hsgamer.demodatarest.controller;

import jakarta.annotation.Nullable;
import me.hsgamer.demodatarest.dto.ResponseCode;
import me.hsgamer.demodatarest.dto.ResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Consumer;

public abstract class CrudController<T, ID, RP extends CrudRepository<T, ID>, REQ, RES> {
    protected final RP repository;

    protected CrudController(RP repository) {
        this.repository = repository;
    }

    protected abstract RES toResponse(T t);

    protected abstract T newEntity();

    protected abstract void updateEntity(T t, REQ req);

    @GetMapping
    public ResponseEntity<ResponseDTO<List<RES>>> getAll(@RequestParam("page") @Nullable Integer page, @RequestParam("size") @Nullable Integer size, @RequestParam("sort") @Nullable List<String> sort) {
        Map<String, Object> extra = new HashMap<>();
        List<RES> list = new ArrayList<>();
        Consumer<T> addToList = t -> list.add(toResponse(t));

        if (page != null && repository instanceof PagingAndSortingRepository) {
            //noinspection unchecked
            PagingAndSortingRepository<T, ID> pagingAndSortingRepository = (PagingAndSortingRepository<T, ID>) repository;

            int finalPage = page;
            int finalSize = size == null ? 10 : size;
            Sort finalSort;
            if (sort == null) {
                finalSort = Sort.unsorted();
            } else {
                List<Sort.Order> orders = new ArrayList<>();
                for (String s : sort) {
                    String[] split = s.split(",");
                    Sort.Direction direction = Sort.Direction.ASC;
                    if (split.length > 1) {
                        direction = Sort.Direction.fromString(split[1]);
                    }
                    Sort.Order order = new Sort.Order(direction, split[0]);
                    if (split.length > 2 && "ignoreCase".equalsIgnoreCase(split[2])) {
                        order = order.ignoreCase();
                    }
                    orders.add(order);
                }
                finalSort = Sort.by(orders);
            }

            Pageable pageable = PageRequest.of(finalPage, finalSize, finalSort);

            Page<T> pageResult = pagingAndSortingRepository.findAll(pageable);

            pageResult.forEach(addToList);

            extra.put("totalPages", pageResult.getTotalPages());
            extra.put("totalElements", pageResult.getTotalElements());
            extra.put("currentPage", pageResult.getNumber());
            extra.put("currentElements", pageResult.getNumberOfElements());
            extra.put("hasNext", pageResult.hasNext());
            extra.put("hasPrevious", pageResult.hasPrevious());
        } else {
            Iterable<T> iterable = repository.findAll();
            iterable.forEach(addToList);
        }

        return ResponseEntity.ok(new ResponseDTO<>(true, ResponseCode.SUCCESS, "OK", list, extra));
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseDTO<RES>> getById(@PathVariable ID id) {
        Optional<T> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO<>(false, ResponseCode.NOT_FOUND, "Not found", null, null));
        }

        T t = optional.get();
        RES res = toResponse(t);
        return ResponseEntity.ok(new ResponseDTO<>(true, ResponseCode.SUCCESS, "OK", res, null));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<RES>> create(REQ req) {
        T t = newEntity();
        updateEntity(t, req);
        T saved = repository.save(t);
        RES res = toResponse(saved);
        return ResponseEntity.ok(new ResponseDTO<>(true, ResponseCode.SUCCESS, "OK", res, null));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<RES>> update(@RequestParam ID id, REQ req) {
        Optional<T> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO<>(false, ResponseCode.NOT_FOUND, "Not found", null, null));
        }

        T t = optional.get();
        updateEntity(t, req);
        T saved = repository.save(t);
        RES res = toResponse(saved);
        return ResponseEntity.ok(new ResponseDTO<>(true, ResponseCode.SUCCESS, "OK", res, null));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResponseDTO<RES>> delete(@PathVariable ID id) {
        Optional<T> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO<>(false, ResponseCode.NOT_FOUND, "Not found", null, null));
        }

        T t = optional.get();
        repository.delete(t);
        return ResponseEntity.ok(new ResponseDTO<>(true, ResponseCode.SUCCESS, "OK", null, null));
    }
}
