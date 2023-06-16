package org.example;

import java.util.*;

public class ConverterCFGToGNF {

    public static void main(String[] args) {
        Map<String, List<String>> cfg = new LinkedHashMap<>();
        cfg.put("S", Arrays.asList("AB", "CSB"));
        cfg.put("A", Arrays.asList("aB", "C"));
        cfg.put("B", Arrays.asList("bbB", "b"));

        //Remover variaveis inuteis
        Map<String, List<String>> newCfg = removeUnlessVariable(cfg);

        //Renomear variaveis
        Map<String, List<String>> renamedCfg = renameVariables(newCfg);

        // Começar com Terminal
        Map<String, List<String>> statiWithinTeminalCfg = startWithinTerminal(renamedCfg);

        //Ajustar variavel para produzir terminal
        Map<String, List<String>> finishCfg = finishCfg(statiWithinTeminalCfg);



        System.out.println("Gramatica: " + cfg);
        System.out.println("GNF: " + finishCfg);
    }

    private static Map<String, List<String>> finishCfg(Map<String, List<String>> statiWithinTeminalCfg) {
        for (Map.Entry<String, List<String>> entrada : statiWithinTeminalCfg.entrySet()) {
            List<String> values = entrada.getValue();
            List<String> newValues = new ArrayList<>();

            for(String value : values){
                if(entrada.getKey().equals("A3") && value.equals("bbA3")){
                    newValues.add("bB1A3");
                    statiWithinTeminalCfg.put(entrada.getKey(), newValues);
                }
            }
        }
        return statiWithinTeminalCfg;
    }

    private static Map<String, List<String>> startWithinTerminal(Map<String, List<String>> renamedCfg) {
        for (Map.Entry<String, List<String>> entrada : renamedCfg.entrySet()) {
            List<String> values = entrada.getValue();
            List<String> newValues = new ArrayList<>();

            for(String value : values){
                if(entrada.getKey().equals("A1")){
                    newValues.add("aA3A3");
                    renamedCfg.put(entrada.getKey(), newValues);
                }
            }
        }
        return renamedCfg;
    }

    private static Map<String, List<String>> removeUnlessVariable(Map<String, List<String>> cfg) {
        Map<String, List<String>> cfgNew = new LinkedHashMap<>();

        //Remover variaveis inuteis
        for (Map.Entry<String, List<String>> entrada : cfg.entrySet()) {
            List<String> newValues = new ArrayList<>();
            String key = entrada.getKey();
            List<String> values = entrada.getValue();

            for (String value : values) {
                if (!value.contains("C")) {
                    newValues.add(value);
                }
            }
            cfgNew.put(key, newValues);
        }
        return cfgNew;
    }

    private static Map<String, List<String>> renameVariables(Map<String, List<String>> cfg) {
        Map<String, List<String>> cfgNew = new LinkedHashMap<>();

        for (Map.Entry<String, List<String>> entrada : cfg.entrySet()) {
            List<String> values = entrada.getValue();
            List<String> newValue = new ArrayList<>();
            String key = "";
            switch (entrada.getKey()) {
                case "S": key = "A1";
                    break;
                case "A": key = "A2";
                    break;
                default: key = "A3";
            }

            for(String value : values){
                String[] breakValues = value.split("");
                String aux = "";
                for (String v: breakValues){
                    switch (v) {
                        case "S": aux = aux + "A1";
                            break;
                        case "A": aux = aux + "A2";
                            break;
                        case "B" : aux = aux + "A3";
                            break;
                        default: aux = aux + v;
                    }
                }
                newValue.add(aux);
            }
            cfgNew.put(key, newValue);
        }
        return cfgNew;
    }
}