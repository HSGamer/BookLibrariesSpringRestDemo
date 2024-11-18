package me.hsgamer.demodatarest.dto;

import java.util.Map;

public record ResponseDTO<T>(boolean success, String code, String message, T data, Map<String, Object> extra) {
}
