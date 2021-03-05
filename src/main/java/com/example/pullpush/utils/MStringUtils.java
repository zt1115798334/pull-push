package com.example.pullpush.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/11/17 13:43
 * description:
 */
public class MStringUtils {

    /**
     * 对包含 + * ，( ) 进行切分
     *
     * @param text test
     * @return List<String>
     */
    public static List<String> splitStr(String text) {
        return Optional.ofNullable(text).map(t -> splitStr(t, "[+*,()]")).orElse(Lists.newArrayList());
    }

    /**
     * 对包含 + * ，( ) 进行切分
     *
     * @param text test
     * @return List<String>
     */
    public static List<String> splitStr(List<String> text) {
        return text.parallelStream().map(MStringUtils::splitStr).flatMap(Collection::stream).distinct().collect(Collectors.toList());
    }

    /**
     * 对满足规则进行切分
     *
     * @param text test
     * @return List<String>
     */
    public static List<String> splitStr(String text, String regex) {
        return Optional.ofNullable(text).map(t ->
                Arrays.stream(t.split(regex))
                        .filter(StringUtils::isNotEmpty)
                        .collect(Collectors.toList()))
                .orElse(Lists.newArrayList());
    }


    /**
     * 切分成最小粒度的排列组合词
     *
     * @param text test
     * @return List<String>
     */
    public static List<String> splitMinGranularityStr(String text) {
        return Optional.ofNullable(text).map(te -> Arrays.stream(te.split(","))
                .filter(StringUtils::isNotBlank)
                .map(comWord -> {
                    List<List<String>> collect = Arrays.stream(comWord.split("[*]"))
                            .map(word -> Arrays.stream(word.split("[+()]"))
                                    .filter(StringUtils::isNotEmpty)
                                    .collect(Collectors.toList()))
                            .collect(Collectors.toList());
                    return calculateCombination(collect);
                }).flatMap(Collection::parallelStream)
                .distinct()
                .map(String::toLowerCase)
                .collect(Collectors.toList())).orElseGet(Lists::newArrayList);
    }


    /**
     * 算法二，非递归计算所有组合
     *
     * @param inputList 所有数组的列表
     */
    private static List<String> calculateCombination(List<List<String>> inputList) {
        int n = inputList.size();

        List<Integer> combination = IntStream.range(0, n).map(i -> 0).boxed().collect(Collectors.toList());

        int i = 0;
        boolean isContinue;
        List<String> result = Lists.newArrayList();

        do {
            List<String> str = IntStream.range(0, n).boxed()
                    .map(j -> inputList.get(j).get(combination.get(j)))
                    .collect(Collectors.toList());

            result.add(String.join("*", str));

            i++;
            combination.set(n - 1, i);
            for (int j = n - 1; j >= 0; j--) {
                if (combination.get(j) >= inputList.get(j).size()) {
                    combination.set(j, 0);
                    i = 0;
                    if (j - 1 >= 0) {
                        combination.set(j - 1, combination.get(j - 1) + 1);
                    }
                }
            }
            isContinue = false;
            for (Integer integer : combination) {
                if (integer != 0) {
                    isContinue = true;
                    break;
                }
            }
        } while (isContinue);
        return result;
    }

    /**
     * id转换为 #1#，#2#，的格式 方便模糊查询  否则会出现查询不准确的
     *
     * @param text text
     * @return String
     */
    public static String decorateStr(String text) {
        if (StringUtils.isNotBlank(text)) {
            List<Long> longList = Arrays.stream(text.trim().split(",")).
                    map(Long::valueOf)
                    .collect(Collectors.toList());
            return decorateStr(longList);
        }
        return text;
    }


    /**
     * id转换为 #1#，#2#，的格式 方便模糊查询  否则会出现查询不准确的
     *
     * @param text long集合
     * @return String
     */
    public static String decorateStr(List<Long> text) {
        return Optional.ofNullable(text)
                .map(str ->
                        str.stream()
                                .filter(Objects::nonNull)
                                .map(MStringUtils::strAddSign)
                                .collect(Collectors.joining(",")))
                .orElse(StringUtils.EMPTY);
    }

    public static String decorateStrAddElement(String decorateStr, Long element) {
        List<Long> oldStr = decorateRecoveryStr(decorateStr);
        oldStr.add(element);
        oldStr = oldStr.stream().distinct().collect(Collectors.toList());
        return decorateStr(oldStr);

    }

    public static String decorateStrRemoveElement(String decorateStr, Long element) {
        List<Long> oldStr = decorateRecoveryStr(decorateStr);
        oldStr.remove(element);
        return decorateStr(oldStr);
    }

    public static String strAddSign(Object text) {
        return "#" + text + "#";
    }

    /**
     * id转换为 #1#，#2#，的格式 转换为list<Long>
     *
     * @param text text
     * @return List<Long>
     */
    public static List<Long> decorateRecoveryStr(String text) {
        return Optional.ofNullable(text).map(str -> Arrays.stream(text.replace("#", "").split(","))
                .filter(StringUtils::isNotBlank)
                .map(Long::valueOf).collect(Collectors.toList())).orElse(Lists.newArrayList());
    }
}
