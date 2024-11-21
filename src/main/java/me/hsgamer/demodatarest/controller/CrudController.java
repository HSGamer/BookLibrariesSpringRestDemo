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

public interface CrudController<T, ID, RP extends CrudRepository<T, ID>, REQ, RES> {
    static Sort getSort(List<String> sort) {
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
        return Sort.by(orders);
    }

    static Pageable getPageable(int page, Integer size, Sort sort) {
        return PageRequest.of(page, size == null ? 10 : size, sort == null ? Sort.unsorted() : sort);
    }

    default ResponseEntity<ResponseDTO<List<RES>>> toResponseEntity(Page<T> page) {
        List<RES> list = new ArrayList<>();
        for (T t : page) {
            RES res = toResponse(t);
            list.add(res);
        }

        Map<String, Object> extra = new HashMap<>();
        extra.put("totalPages", page.getTotalPages());
        extra.put("totalElements", page.getTotalElements());
        extra.put("currentPage", page.getNumber());
        extra.put("currentElements", page.getNumberOfElements());
        extra.put("hasNext", page.hasNext());
        extra.put("hasPrevious", page.hasPrevious());

        return ResponseEntity.ok(new ResponseDTO<>(true, ResponseCode.SUCCESS, "OK", list, extra));
    }

    default ResponseEntity<ResponseDTO<List<RES>>> toResponseEntity(Iterable<T> iterable) {
        List<RES> list = new ArrayList<>();
        for (T t : iterable) {
            RES res = toResponse(t);
            list.add(res);
        }

        return ResponseEntity.ok(new ResponseDTO<>(true, ResponseCode.SUCCESS, "OK", list, null));
    }

    default ResponseEntity<ResponseDTO<RES>> toResponseEntity(T entity) {
        return ResponseEntity.ok(new ResponseDTO<>(true, ResponseCode.SUCCESS, "OK", toResponse(entity), null));
    }

    RP getRepository();

    RES toResponse(T t);

    REQ toRequest(T t);

    T newEntity();

    void updateEntity(T t, REQ req);

    @GetMapping
    default ResponseEntity<ResponseDTO<List<RES>>> getAll(@RequestParam("page") @Nullable Integer page, @RequestParam("size") @Nullable Integer size, @RequestParam("sort") @Nullable List<String> sorts) {
        RP repository = getRepository();

        Sort sort = sorts == null ? null : getSort(sorts);
        Pageable pageable = page == null ? null : getPageable(page, size, sort);

        if ((pageable == null && sort == null) || !(repository instanceof PagingAndSortingRepository)) {
            return toResponseEntity(repository.findAll());
        } else {
            //noinspection unchecked
            PagingAndSortingRepository<T, ID> pagingAndSortingRepository = (PagingAndSortingRepository<T, ID>) repository;

            if (pageable != null) {
                return toResponseEntity(pagingAndSortingRepository.findAll(pageable));
            } else {
                return toResponseEntity(pagingAndSortingRepository.findAll(sort));
            }
        }
    }

    @GetMapping("{id}")
    default ResponseEntity<ResponseDTO<RES>> getById(@PathVariable ID id) {
        RP repository = getRepository();

        Optional<T> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO<>(false, ResponseCode.NOT_FOUND, "Not found", null, null));
        }

        return toResponseEntity(optional.get());
    }

    @PostMapping
    default ResponseEntity<ResponseDTO<RES>> create(REQ req) {
        RP repository = getRepository();
        T t = newEntity();
        updateEntity(t, req);
        T saved = repository.save(t);
        return toResponseEntity(saved);
    }

    @PutMapping
    default ResponseEntity<ResponseDTO<RES>> update(@RequestParam ID id, REQ req) {
        RP repository = getRepository();
        Optional<T> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO<>(false, ResponseCode.NOT_FOUND, "Not found", null, null));
        }

        T t = optional.get();
        updateEntity(t, req);
        T saved = repository.save(t);
        return toResponseEntity(saved);
    }

    @DeleteMapping("{id}")
    default ResponseEntity<ResponseDTO<RES>> delete(@PathVariable ID id) {
        RP repository = getRepository();
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
