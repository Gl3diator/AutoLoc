package tn.esprit.autoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.service.IEmployeService;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository repository;
    private final IAgenceRepository agenceRepository;

    @Override
    public Employe create(Employe entity) {
        if (entity.getIdEmploye() != null) {
            throw new IllegalArgumentException(
                "Un nouvel objet ne doit pas avoir d'identifiant");
        }
        validate(entity);
        return repository.save(entity);
    }

    @Override
    public Employe findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Employe", id));
    }

    @Override
    public List<Employe> findAll() {
        return repository.findAll();
    }

    @Override
    public Employe update(Long id, Employe entity) {
        Employe existing = findById(id);
        validate(entity);
        existing.setNom(entity.getNom());
        existing.setPrenom(entity.getPrenom());
        existing.setRole(entity.getRole());
        return repository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Employe", id);
        }
        repository.deleteById(id);
    }

    private void validate(Employe e) {
        if (e.getNom() == null || e.getNom().isBlank()
                || e.getRole() == null) {
            throw new IllegalArgumentException("Nom et rôle obligatoires");
        }
    }

    @Override
    @Transactional
    public void affecterEmployeAAgence(Long employeId, Long agenceId) {
        Employe entity = findById(employeId);

        Agence agence = agenceRepository.findById(agenceId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Agence", agenceId));

        entity.setAgence(agence);
        repository.save(entity);
    }

    @Override
    @Transactional
    public void desaffecterEmployeDeAgence(Long employeId) {
        Employe entity = findById(employeId);

        if (entity.getAgence() == null) {
            return;
        }

        entity.setAgence(null);
        repository.save(entity);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
