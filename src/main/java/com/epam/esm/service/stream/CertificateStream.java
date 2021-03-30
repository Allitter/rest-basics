package com.epam.esm.service.stream;

import com.epam.esm.model.Certificate;

public interface CertificateStream extends ServiceStream<Certificate> {

    CertificateStream nameLike(String name);

    CertificateStream descriptionLike(String name);

    CertificateStream tagNameLike(String tagName);

    CertificateStream sortName(boolean asc);

    CertificateStream sortCreateDate(boolean asc);
}
