package br.com.bibliaf.dto;

public record LoginResponseDto(String token, String role, Long id) {
}
