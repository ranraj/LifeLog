package com.ols.lifelog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ols.lifelog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventLogTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventLogType.class);
        EventLogType eventLogType1 = new EventLogType();
        eventLogType1.setId(1L);
        EventLogType eventLogType2 = new EventLogType();
        eventLogType2.setId(eventLogType1.getId());
        assertThat(eventLogType1).isEqualTo(eventLogType2);
        eventLogType2.setId(2L);
        assertThat(eventLogType1).isNotEqualTo(eventLogType2);
        eventLogType1.setId(null);
        assertThat(eventLogType1).isNotEqualTo(eventLogType2);
    }
}
