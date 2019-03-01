/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package mfix.core.pattern;

import mfix.common.util.Constant;
import mfix.common.util.JavaFile;
import mfix.common.util.Pair;
import mfix.core.node.abs.CodeAbstraction;
import mfix.core.node.abs.TermFrequency;
import mfix.core.node.ast.MethDecl;
import mfix.core.node.ast.Node;
import mfix.core.node.match.Matcher;
import mfix.core.node.parser.NodeParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: Jiajun
 * @date: 2019-01-15
 */
public class PatternExtractor {

    public Set<Pattern> extractPattern(Set<Pair<String, String>> fixPairs) {
        Set<Pattern> nodes = new HashSet<>();
        for(Pair<String, String> pair : fixPairs) {
            nodes.addAll(extractPattern(pair.getFirst(), pair.getSecond()));
        }
        return nodes;
    }

    public Set<Pattern> extractPattern(String srcFile, String tarFile) {
        CompilationUnit srcUnit = JavaFile.genASTFromFileWithType(srcFile, null);
        CompilationUnit tarUnit = JavaFile.genASTFromFileWithType(tarFile, null);
        List<Pair<MethodDeclaration, MethodDeclaration>> matchMap = new Matcher().match(srcUnit, tarUnit);
        NodeParser nodeParser = new NodeParser();
        Set<Pattern> patterns = new HashSet<>();
//        CodeAbstraction abstraction = new TF_IDF(srcFile, Constant.TF_IDF_FREQUENCY);
        CodeAbstraction abstraction = new TermFrequency(Constant.TOKEN_FREQENCY);
//        ElementCounter counter = new ElementCounter();
//        counter.open();
//        counter.loadCache();

        for (Pair<MethodDeclaration, MethodDeclaration> pair : matchMap) {
            nodeParser.setCompilationUnit(srcFile, srcUnit);
            Node srcNode = nodeParser.process(pair.getFirst());
            nodeParser.setCompilationUnit(tarFile, tarUnit);
            Node tarNode = nodeParser.process(pair.getSecond());

            if(srcNode.toSrcString().toString().equals(tarNode.toSrcString().toString())) {
                continue;
            }

            if(new Matcher().greedyMatch((MethDecl) srcNode, (MethDecl) tarNode)) {
                Set<Node> nodes = tarNode.getConsideredNodesRec(new HashSet<>(), false);
                Set<Node> temp;
                for(Node node : nodes) {
                    if (node.getBindingNode() != null) {
                        node.getBindingNode().setConsidered(true);
                    }
                    temp = node.expand(new HashSet<>());
                    for(Node n : temp) {
                        if (n.getBindingNode() != null) {
                            n.getBindingNode().setConsidered(true);
                        }
                    }
                }
//                srcNode.doAbstraction(counter);
                srcNode.doAbstractionNew(abstraction.lazyInit());
                tarNode.doAbstractionNew(abstraction);
                patterns.add(new Pattern(srcNode));
            }
        }

//        counter.close();
        return patterns;
    }
}
