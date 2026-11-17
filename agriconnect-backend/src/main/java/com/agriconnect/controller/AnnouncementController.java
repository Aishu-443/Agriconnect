package com.agriconnect.controller;

import com.agriconnect.dto.AnnouncementDTO;
import com.agriconnect.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping
    public ResponseEntity<List<AnnouncementDTO>> getAllAnnouncements() {
        List<AnnouncementDTO> announcements = announcementService.getAll();
        return ResponseEntity.ok(announcements);
    }

    @PostMapping
    public ResponseEntity<AnnouncementDTO> addAnnouncement(@RequestBody Map<String, String> payload) {
        if (payload == null || !payload.containsKey("title") || !payload.containsKey("content")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payload must include title and content");
        }
        String title = payload.get("title");
        String content = payload.get("content");
        AnnouncementDTO created = announcementService.addAnnouncement(title, content);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.ok(Map.of("message", "Announcement deleted successfully", "id", id.toString()));
    }
}
