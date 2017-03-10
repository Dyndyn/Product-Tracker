package com.softserve.if072.mvcapp.controller;

import com.softserve.if072.common.model.History;
import com.softserve.if072.mvcapp.service.HistoryService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * The HistoryController class handles requests for "/history" and renders appropriate view
 * REST server
 *
 * @author Igor Kryviuk
 */
@Controller
@RequestMapping("/history")
public class HistoryController {
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * Handles requests for getting all history records for current user
     *
     * @param model - a map that will be handed off to the view for rendering the data to the client
     * @return string with appropriate view name
     */
    @GetMapping
    public String getHistories(Model model) {
        List<History> histories = historyService.getByUserId();
        if (CollectionUtils.isNotEmpty(histories)) {
            model.addAttribute("histories", histories);
            return "history";
        }
        return "emptyHistory";
    }

    /**
     * Handles requests for deleting a record from the history of current user
     *
     * @param historyId - history unique identifier
     * @return string with appropriate view name
     */
    @GetMapping("/delete/{historyId}")
    public String deleteRecordFromHistory(@PathVariable int historyId) {
        historyService.deleteRecordFromHistory(historyId);
        return "redirect:/history";
    }

    /**
     * Handles requests for deleting all records from the history of current user
     *
     * @return string with appropriate view name
     */
    @GetMapping("/delete")
    public String deleteAllRecordsFromHistory() {
        historyService.deleteAllRecordsFromHistory();
        return "emptyHistory";
    }
}
