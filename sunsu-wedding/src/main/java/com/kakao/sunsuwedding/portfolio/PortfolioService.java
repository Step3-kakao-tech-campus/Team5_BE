package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.errors.exception.Exception404;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioDTO;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioListItemDTO;
import com.kakao.sunsuwedding.portfolio.dto.PriceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;

    public List<PortfolioListItemDTO> getPortfolios(PageRequest pageRequest) {
        Page<Portfolio> portfolios = portfolioRepository.findAll(pageRequest);

        // TODO: 포트폴리오별 대표 이미지 불러오기
        Resource[] images = null;

        return PortfolioDTOConverter.toListItemDTO(portfolios, images);
    }

    public PortfolioDTO getPortfolioById(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 포트폴리오를 찾을 수 없습니다."));

        // TODO: ImageItemService 에서 포트폴리오 이미지 가져오기
        Resource[] images = null;

        // TODO: PriceItemService 에서 가격 정보 가져오기
        PriceDTO priceDTO = null;

        return PortfolioDTOConverter.toPortfolioDTO(portfolio, images, priceDTO);
    }
}
