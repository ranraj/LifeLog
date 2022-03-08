package com.ols.lifelog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ols.lifelog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TagsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tags.class);
        Tags tags1 = new Tags();
        tags1.setId(1L);
        Tags tags2 = new Tags();
        tags2.setId(tags1.getId());
        assertThat(tags1).isEqualTo(tags2);
        tags2.setId(2L);
        assertThat(tags1).isNotEqualTo(tags2);
        tags1.setId(null);
        assertThat(tags1).isNotEqualTo(tags2);
    }
}
