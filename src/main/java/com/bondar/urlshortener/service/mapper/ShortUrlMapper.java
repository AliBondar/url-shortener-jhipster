package com.bondar.urlshortener.service.mapper;

import com.bondar.urlshortener.domain.ShortUrl;
import com.bondar.urlshortener.service.dto.ShortUrlDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShortUrl} and its DTO {@link ShortUrlDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShortUrlMapper extends EntityMapper<ShortUrlDTO, ShortUrl> {}
