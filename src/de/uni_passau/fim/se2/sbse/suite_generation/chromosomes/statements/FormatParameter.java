package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements;

public class FormatParameter {
    public static String formatParameter(Object param) {
        if (param == null) {
            return "null";
        } else if (param instanceof String) {
            return "\"" + escapeString((String) param) + "\"";
        } else if (param instanceof Character) {
            return "'" + escapeChar((Character) param) + "'";
        } else if (param instanceof Boolean) {
            return param.toString();
        } else if (param instanceof Float) {
            return param + "f";
        } else if (param instanceof Long) {
            return param + "L";
        } else if (param instanceof Double) {
            String str = param.toString();
            if (!str.contains(".") && !str.toLowerCase().contains("e")) {
                str += ".0";
            }
            return str;
        } else if (param instanceof Number) {
            return param.toString();
        } else {
            return "null";
        }
    }

    private static String escapeString(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String escapeChar(char c) {
        if (c == '\'') {
            return "\\'";
        } else if (c == '\\') {
            return "\\\\";
        } else if (c == '\n') {
            return "\\n";
        } else if (c == '\r') {
            return "\\r";
        } else if (c == '\t') {
            return "\\t";
        } else {
            return Character.toString(c);
        }
    }
}
