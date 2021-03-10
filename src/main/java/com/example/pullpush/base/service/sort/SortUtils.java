package com.example.pullpush.base.service.sort;

import org.springframework.data.domain.Sort;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 16:05
 * description:
 */
public class SortUtils {

    public static Sort basicSort() {
        return basicSort("desc", "id");
    }

    public static Sort basicSort(String orderType, String orderField) {
        return Sort.by(Sort.Direction.fromString(orderType), orderField);
    }

    public static Sort basicSort(SortDto... dtoArr) {
        Sort result = null;
        for (SortDto dto : dtoArr) {
            if (result == null) {
                result = Sort.by(Sort.Direction.fromString(dto.getOrderType()), dto.getOrderField());
            } else {
                result = result.and(Sort.by(Sort.Direction.fromString(dto.getOrderType()), dto.getOrderField()));
            }
        }
        return result;
    }
}
