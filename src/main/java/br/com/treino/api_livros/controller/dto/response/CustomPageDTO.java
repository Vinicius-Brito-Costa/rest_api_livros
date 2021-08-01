package br.com.treino.api_livros.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class CustomPageDTO<T> {

    private List<T> content;
    private CustomPageInfo pageable;

    public CustomPageDTO(Page<T> page) {
        this.content = page.getContent();
        this.pageable = new CustomPageInfo(
                page.isFirst(),
                page.isLast(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getSize(),
                page.getNumber()
        );
    }

    @Data
    @AllArgsConstructor
    static class CustomPageInfo{
        private Boolean first;
        private Boolean last;
        private Long totalElements;
        private Integer totalPages;
        private Integer size;
        private Integer number;
    }
}
