package com.agriconnect.service;

import com.agriconnect.dto.AnnouncementDTO;
import com.agriconnect.model.Announcement;
import com.agriconnect.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public List<AnnouncementDTO> getAll() {
        return announcementRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public AnnouncementDTO addAnnouncement(String title, String content) {
        if (title == null || title.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Content is required");
        }

        Announcement announcement = Announcement.builder()
                .title(title.trim())
                .content(content.trim())
                .publishedDate(LocalDate.now())
                .build();

        Announcement saved = announcementRepository.save(announcement);
        return toDTO(saved);
    }

    public void deleteAnnouncement(Long id) {
        if (!announcementRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Announcement not found with id: " + id);
        }
        announcementRepository.deleteById(id);
    }

    public AnnouncementDTO toDTO(Announcement a) {
        if (a == null) return null;
        return AnnouncementDTO.builder()
                .id(a.getId())
                .title(a.getTitle())
                .content(a.getContent())
                .publishedDate(a.getPublishedDate())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
