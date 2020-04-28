/**
 * Copyright (c)  2011-2020 Panguso, Inc.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of Panguso,
 * Inc. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with Panguso.
 */
package data.lab.ongdb.algo.simhash;

import data.isiteam.zdr.wltea.analyzer.cfg.Configuration;
import data.isiteam.zdr.wltea.analyzer.core.IKSegmenter;
import data.isiteam.zdr.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 文档分词
 *
 * @author louxuezheng@hotmail.com
 */
public final class WordsSegment {
    /**
     * 分词
     *
     * @param query 字符串
     * @return
     */
    public static List<String> getCutWords(String query) {

        List<String> words = new ArrayList<>();

        boolean useSmart = false; // true 用智能分词，false 细粒度

        Configuration cfg = new Configuration(useSmart);
        StringReader input = new StringReader(query.trim());
        IKSegmenter ikSegmenter = new IKSegmenter(input, cfg);

        try {
            for (Lexeme lexeme = ikSegmenter.next(); lexeme != null; lexeme = ikSegmenter.next()) {
                words.add(lexeme.getLexemeText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }
}