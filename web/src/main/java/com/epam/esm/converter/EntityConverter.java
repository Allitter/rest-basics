package com.epam.esm.converter;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;

import java.util.List;
import java.util.stream.Collectors;

public final class EntityConverter {

    public static CertificateDto certificateToDto(Certificate certificate) {
        List<TagDto> tagDtos = certificate.getTags().stream()
                .map(EntityConverter::tagToDto)
                .collect(Collectors.toList());

        return new CertificateDto(
                certificate.getId(),
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                certificate.getCreateDate(),
                certificate.getLastUpdateDate(),
                tagDtos);
    }

    public static Certificate dtoToCertificate(CertificateDto dto) {
        List<Tag> tags = dto.getTags().stream()
                .map(EntityConverter::dtoToTag)
                .collect(Collectors.toList());

        return new Certificate.Builder()
                .setId(dto.getId())
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setPrice(dto.getPrice())
                .setDuration(dto.getDuration())
                .setCreateDate(dto.getCreateDate())
                .setLastUpdateDate(dto.getLastUpdateDate())
                .setTags(tags)
                .build();
    }

    public static Tag dtoToTag(TagDto dto) {
        return new Tag(dto.getId(), dto.getName());
    }

    public static TagDto tagToDto(Tag tag) {
        return new TagDto(tag.getId(), tag.getName());
    }

    private EntityConverter(){
    }
}
