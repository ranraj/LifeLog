package com.ols.lifelog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ols.lifelog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventLogBookTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventLogBook.class);
        EventLogBook eventLogBook1 = new EventLogBook();
        eventLogBook1.setId(1L);
        EventLogBook eventLogBook2 = new EventLogBook();
        eventLogBook2.setId(eventLogBook1.getId());
        assertThat(eventLogBook1).isEqualTo(eventLogBook2);
        eventLogBook2.setId(2L);
        assertThat(eventLogBook1).isNotEqualTo(eventLogBook2);
        eventLogBook1.setId(null);
        assertThat(eventLogBook1).isNotEqualTo(eventLogBook2);
    }
}
