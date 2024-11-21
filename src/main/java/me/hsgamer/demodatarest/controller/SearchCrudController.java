package me.hsgamer.demodatarest.controller;

import jakarta.annotation.Nullable;
import me.hsgamer.demodatarest.dto.ResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public interface SearchCrudController<T, ID, RP extends CrudRepository<T, ID> & JpaSpecificationExecutor<T>, REQ, RES> extends CrudController<T, ID, RP, REQ, RES> {
    @SafeVarargs
    static <T> Specification<T> allOf(Supplier<Specification<T>>... suppliers) {
        List<Specification<T>> specifications = Arrays.stream(suppliers).map(Supplier::get).filter(Objects::nonNull).toList();
        return Specification.allOf(specifications);
    }

    @SafeVarargs
    static <T> Specification<T> anyOf(Supplier<Specification<T>>... suppliers) {
        List<Specification<T>> specifications = Arrays.stream(suppliers).map(Supplier::get).filter(Objects::nonNull).toList();
        return Specification.anyOf(specifications);
    }

    Specification<T> getSpecification(Map<String, String> params);

    @GetMapping("/search")
    default ResponseEntity<ResponseDTO<List<RES>>> search(@RequestParam("page") @Nullable Integer page, @RequestParam("size") @Nullable Integer size, @RequestParam("sort") @Nullable List<String> sorts, @RequestParam Map<String, String> params) {
        RP repository = getRepository();
        Specification<T> specification = getSpecification(params);
        if (specification == null) {
            return getAll(page, size, sorts);
        }

        Sort sort = sorts == null ? null : CrudController.getSort(sorts);
        Pageable pageable = page == null ? null : CrudController.getPageable(page, size, sort);

        if (pageable != null) {
            return toResponseEntity(repository.findAll(specification, pageable));
        } else if (sort != null) {
            return toResponseEntity(repository.findAll(specification, sort));
        } else {
            return toResponseEntity(repository.findAll(specification));
        }
    }
}
