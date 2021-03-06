package com.softserve.if072.mvcapp.controller;

import com.softserve.if072.common.model.Action;
import com.softserve.if072.common.model.History;
import com.softserve.if072.common.model.RestResponsePage;
import com.softserve.if072.common.model.User;
import com.softserve.if072.common.model.dto.HistorySearchDTO;
import com.softserve.if072.mvcapp.service.HistoryService;
import com.softserve.if072.mvcapp.service.PdfCreatorService;
import com.softserve.if072.mvcapp.service.ProductPageService;
import com.softserve.if072.mvcapp.service.UserService;
import com.softserve.if072.mvcapp.test.utils.HistoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * The HistoryControllerTest class is used to test CartController class methods
 *
 * @author Igor Kryviuk
 */
@RunWith(MockitoJUnitRunner.class)
public class HistoryControllerTest {
    private static final int FIRST_HISTORY_ITEM_ID = 1;
    private static final int SECOND_HISTORY_ITEM_ID = 2;
    private static final int FIRST_HISTORY_ITEM_AMOUNT = 5;
    private static final int SECOND_HISTORY_ITEM_AMOUNT = 3;
    private static final Timestamp FIRST_HISTORY_ITEM_USEDDATE = new Timestamp(System.currentTimeMillis());
    private static final Timestamp SECOND_HISTORY_ITEM_USEDDATE = new Timestamp(System.currentTimeMillis());
    private static final int CURRENT_USER_ID = 4;
    private static final int HISTORY_ID = 32;
    @Mock
    private HistoryService historyService;
    @Mock
    private ProductPageService productPageService;
    @Mock
    private UserService userService;
    @Mock
    private PdfCreatorService pdfCreatorService;
    private HistoryController historyController;
    private MockMvc mockMvc;

    @Before
    public void setup() throws ClassNotFoundException, NoSuchMethodException {
        historyController = new HistoryController(historyService, productPageService, userService, pdfCreatorService);
        mockMvc = standaloneSetup(historyController)
                .setViewResolvers(new InternalResourceViewResolver("/WEB-INF/views/history/", ".jsp"))
                .build();

        User user = new User();
        user.setId(1);
        when(userService.getCurrentUser()).thenReturn(user);
        when(productPageService.getAllCategories(anyInt())).thenReturn(new ArrayList<>());
    }

    @Test
    public void getHistories_ShouldReturnHistoryViewName_ModelShouldHaveAppropriateAttributes() throws Exception {
        History history1 = HistoryBuilder.getDefaultHistory(FIRST_HISTORY_ITEM_ID, CURRENT_USER_ID
                , FIRST_HISTORY_ITEM_AMOUNT, FIRST_HISTORY_ITEM_USEDDATE, Action.PURCHASED);
        History history2 = HistoryBuilder.getDefaultHistory(SECOND_HISTORY_ITEM_ID, CURRENT_USER_ID
                , SECOND_HISTORY_ITEM_AMOUNT, SECOND_HISTORY_ITEM_USEDDATE, Action.USED);
        List<History> historyList = Arrays.asList(history1, history2);

        PageImpl<History> historiesPage = new PageImpl<>(historyList);
        when(historyService.getHistorySearchPage(any(), anyInt(), anyInt())).thenReturn(historiesPage);


        mockMvc.perform(get("/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("history"))
                .andExpect(forwardedUrl("/WEB-INF/views/history/history.jsp"))
                .andExpect(model().attributeExists("historiesPage"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("historySearchDTO"))
                .andExpect(model().attribute("historiesPage", hasItem(
                        allOf(
                                hasProperty("id", is(FIRST_HISTORY_ITEM_ID)),
                                hasProperty("user", hasProperty("name"
                                        , is(String.format("user%d", CURRENT_USER_ID)))),
                                hasProperty("product", hasProperty("name"
                                        , is(String.format("product%d", FIRST_HISTORY_ITEM_ID)))),
                                hasProperty("amount", is(FIRST_HISTORY_ITEM_AMOUNT)),
                                hasProperty("usedDate", is(FIRST_HISTORY_ITEM_USEDDATE)),
                                hasProperty("action", is(Action.PURCHASED))
                        ))
                ))
                .andExpect(model().attribute("historiesPage", hasItem(
                        allOf(
                                hasProperty("id", is(SECOND_HISTORY_ITEM_ID)),
                                hasProperty("user", hasProperty("name"
                                        , is(String.format("user%d", CURRENT_USER_ID)))),
                                hasProperty("product", hasProperty("name"
                                        , is(String.format("product%d", SECOND_HISTORY_ITEM_ID)))),
                                hasProperty("amount", is(SECOND_HISTORY_ITEM_AMOUNT)),
                                hasProperty("usedDate", is(SECOND_HISTORY_ITEM_USEDDATE)),
                                hasProperty("action", is(Action.USED))
                        ))
                ));

        verify(historyService).getHistorySearchPage(any(), anyInt(), anyInt());
        verifyZeroInteractions(historyService);
    }

    @Test
    public void searchHistories_ShouldReturnHistoryViewName_ModelShouldHaveAppropriateAttributes() throws Exception {
        History historyA = HistoryBuilder.getDefaultHistory(FIRST_HISTORY_ITEM_ID, CURRENT_USER_ID
                , FIRST_HISTORY_ITEM_AMOUNT, FIRST_HISTORY_ITEM_USEDDATE, Action.PURCHASED);
        List<History> historiesList = Arrays.asList(historyA);
        Page<History> histories = new PageImpl<History>(historiesList, new PageRequest(0, 25), historiesList.size());

        mockMvc.perform(post("/history").sessionAttr("historySearchDTO", new HistorySearchDTO()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/history"))
                .andExpect(model().attributeExists("historySearchDTO"));

        verifyZeroInteractions(historyService);
    }

    @Test
    public void getHistories_ShouldReturnEmptyPage() throws Exception {
        when(historyService.getHistorySearchPage(any(), anyInt(), anyInt())).thenReturn(new RestResponsePage<>());
        mockMvc.perform(get("/history")
                .param("pageNumber", "1")
                .param("pageSize", "25"))
                .andExpect(status().isOk())
                .andExpect(view().name("history"))
                .andExpect(forwardedUrl("/WEB-INF/views/history/history.jsp"))
                .andExpect(model().attributeDoesNotExist("historiesPage"));

        verify(historyService).getHistorySearchPage(any(), anyInt(), anyInt());
        verifyZeroInteractions(historyService);
    }

    @Test
    public void deleteRecordFromHistory_ShouldRedirectToHistoryHandler() throws Exception {
        mockMvc.perform(get("/history/delete/{historyId}", HISTORY_ID))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/history"));
    }

    @Test
    public void deleteAllRecordsFromHistory_ShouldForwardToEmptyHistoryPage() throws Exception {
        mockMvc.perform(get("/history/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("emptyHistory"))
                .andExpect(forwardedUrl("/WEB-INF/views/history/emptyHistory.jsp"));
    }

    @Test
    public void getPDF_ShouldResponsePDF() throws Exception {
        mockMvc.perform(get("/history/getpdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/pdf"));
    }
}