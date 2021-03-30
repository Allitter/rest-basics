package com.epam.esm.controller;

import com.epam.esm.controller.dto.CertificateDto;
import com.epam.esm.controller.dto.CertificateQueryObject;
import com.epam.esm.model.Certificate;
import com.epam.esm.service.CertificateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping()
    public List<Certificate> displayAll(CertificateQueryObject query) {
        return certificateService.findCertificatesByQueryObject(query);
    }

    @GetMapping(value = "/{id}")
    public CertificateDto displayCertificate(@PathVariable int id) {
        return certificateService.findById(id);
    }

    @PostMapping()
    public void addCertificate(@RequestBody CertificateDto dto) {
        certificateService.add(dto);
    }

    @PutMapping(value = "/{id}")
    public void updateCertificate(@PathVariable int id, @RequestBody CertificateDto dto) {
        dto.setId(id);
        certificateService.update(dto);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteCertificate(@PathVariable int id) {
        certificateService.remove(id);
    }
}
