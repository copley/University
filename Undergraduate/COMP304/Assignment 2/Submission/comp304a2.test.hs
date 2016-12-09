import Test.HUnit
import qualified COMP304A2 as A2

testBinTree1 = A2.Node 1 (A2.Node 2 (A2.Node 4 A2.Empty (A2.Node 3 A2.Empty A2.Empty)) A2.Empty) (A2.Node 8 A2.Empty A2.Empty)
testBinTree2 = A2.Node 2 (A2.Node 3 A2.Empty A2.Empty) (A2.Node 4 A2.Empty A2.Empty)
testBinTree2Reflected = A2.Node 2 (A2.Node 4 A2.Empty A2.Empty) (A2.Node 3 A2.Empty A2.Empty)

hasbtTest1 = TestCase (assertEqual "Tree contains element 1"
                        (A2.hasbt 1 testBinTree1) True)

hasbtTest2 = TestCase (assertEqual "Tree contains element 8"
                        (A2.hasbt 8 testBinTree1) True)

hasbtTest3 = TestCase (assertEqual "Tree dosnt contains element 7"
                        (A2.hasbt 7 testBinTree1) False)


equalbtTest1 = TestCase (assertEqual "Tree should be equal to its self"
                          (A2.equalbt testBinTree1 testBinTree1) True)

equalbtTest2 = TestCase (assertEqual "The two trees arnt equal"
                          (A2.equalbt testBinTree1 testBinTree2) False)


reflecttbtTest1 = TestCase (assertEqual "Should be equal to reflected tree"
                            (A2.reflecttbt testBinTree2) testBinTree2Reflected)


fringebtTest1 = TestCase (assertEqual "Fringe should be [3, 8]"
                          (A2.fringebt testBinTree1) [3, 8])

fringebtTest2 = TestCase (assertEqual "Fringe should be [3, 4]"
                          (A2.fringebt testBinTree2) [3, 4])


fullbtTest1 = TestCase (assertEqual "Tree is not full"
                        (A2.fullbt testBinTree1) False)

fullbtTest2 = TestCase (assertEqual "Tree is full"
                        (A2.fullbt testBinTree2) True)

fullbtTest3 = TestCase (assertEqual "A2.Empty should be full"
                        (A2.fullbt A2.Empty) True)




hasbtfTest1 = TestCase (assertEqual "Tree contains element 1"
                        (A2.hasbtf 1 testBinTree1) True)

hasbtfTest2 = TestCase (assertEqual "Tree contains element 8"
                        (A2.hasbtf 8 testBinTree1) True)

hasbtfTest3 = TestCase (assertEqual "Tree dosnt contains element 7"
                        (A2.hasbtf 7 testBinTree1) False)


reflecttbtfTest1 = TestCase (assertEqual "Should be equal to reflected tree"
                            (A2.reflecttbtf testBinTree2) testBinTree2Reflected)


fringebtfTest1 = TestCase (assertEqual "Fringe should be [3, 8]"
                          (A2.fringebtf testBinTree1) [3, 8])

fringebtfTest2 = TestCase (assertEqual "Fringe should be [3, 4]"
                          (A2.fringebtf testBinTree2) [3, 4])


fullbtfTest1 = TestCase (assertEqual "Tree is not full"
                        (A2.fullbtf testBinTree1) False)

fullbtfTest2 = TestCase (assertEqual "Tree is full"
                        (A2.fullbtf testBinTree2) True)

fullbtfTest3 = TestCase (assertEqual "A2.Empty should be full"
                        (A2.fullbtf A2.Empty) True)


bst1 = A2.Empty

bst2 = (A2.Node 5 A2.Empty A2.Empty)
bst2' = A2.insert 5 bst1

insertTest1 = TestCase (assertEqual "Trees should be equal"
                        bst2' bst2)

bst3 = (A2.Node 5 (A2.Node 4 A2.Empty A2.Empty) A2.Empty)
bst3' = A2.insert 4 bst2'

insertTest2 = TestCase (assertEqual "Trees should be equal"
                        bst3' bst3)


bst4 = (A2.Node 5 (A2.Node 4 (A2.Node 3 (A2.Node 2 A2.Empty A2.Empty) A2.Empty) A2.Empty) (A2.Node 6 A2.Empty A2.Empty))
bst4' = A2.insert 6 (A2.insert 2 (A2.insert 3 bst3'))

insertTest3 = TestCase (assertEqual "Trees should be equal"
                        bst4' bst4)


hasTest1 = TestCase (assertEqual "Tree should contain 2"
                    (A2.has 2 bst4) True)

hasTest2 = TestCase (assertEqual "Tree should contain 4"
                    (A2.has 4 bst4) True)


bst5 = (A2.Node 5 (A2.Node 4 (A2.Node 3 A2.Empty A2.Empty) A2.Empty) (A2.Node 6 A2.Empty A2.Empty))

removeTest1 = TestCase (assertEqual "Trees should be equal"
                        (A2.delete 2 bst4) bst5)

bst6 = (A2.Node 5 (A2.Node 4 (A2.Node 2 A2.Empty A2.Empty) A2.Empty) (A2.Node 6 A2.Empty A2.Empty))

removeTest2 = TestCase (assertEqual "Trees should be equal"
                        (A2.delete 3 bst4) bst6)

bst7 = (A2.Node 6 (A2.Node 4 (A2.Node 3 (A2.Node 2 A2.Empty A2.Empty) A2.Empty) A2.Empty) A2.Empty)

removeTest3 = TestCase (assertEqual "Trees should be equal"
                        (A2.delete 5 bst4) bst7)


flattenTest1 = TestCase (assertEqual "The flattened tree should be [2,3,4,5,6]"
                        (A2.flatten bst4) [2,3,4,5,6])



testGraph = [('a', 1, 'b'), ('b', 1, 'd'), ('a', 1, 'c'), ('c', 1, 'e'), ('e', 1, 'd'), ('a', 1, 'f'), ('g', 1, 'h')]

findAllPathsTest1 = TestCase (assertEqual "Path contains only one edge"
                              (A2.findAllPaths 'a' 'f' testGraph) [[('a', 1, 'f')]])

findAllPathsTest2 = TestCase (assertEqual "Should be two paths"
                              (A2.findAllPaths 'a' 'd' testGraph) [[('a', 1, 'b'), ('b', 1, 'd')],
                                                                  [('a', 1, 'c'), ('c', 1, 'e'), ('e', 1, 'd')]])

findAllPathsTest3 = TestCase (assertEqual "Should be no paths from b to h"
                              (A2.findAllPaths 'b' 'h' testGraph) [])

findAllPathsTest4 = TestCase (assertEqual "Should be no paths from b to k (x isnt a node)"
                              (A2.findAllPaths 'b' 'x' testGraph) [])


reachableTest1 = TestCase (assertEqual "F is only reachable from A"
                            (A2.reachable 'b' 'f' testGraph) True)

reachableTest2 = TestCase (assertEqual "F is only reachable from A"
                            (A2.reachable 'a' 'f' testGraph) True)

reachableTest3 = TestCase (assertEqual "D should be reachable from A"
                            (A2.reachable 'a' 'd' testGraph) True)

reachableTest4 = TestCase (assertEqual "H should be reachable from G"
                          (A2.reachable 'g' 'h' testGraph) True)


minCostPathTest1 = TestCase (assertEqual "Min cost from A to D is 2"
                        ((A2.minCostPath 'a' 'd' testGraph)) [ ('a', 1, 'b'), ('b', 1, 'd') ])

minCostPathTest2 = TestCase (assertEqual "Min cost from H to G is 1"
                        ((A2.minCostPath 'g' 'h' testGraph)) [('g', 1, 'h')])


c = A2.cliques testGraph

cliquesTest1 = TestCase (assertEqual "Should be two cliques"
                        (length c) 2)

cliquesTest2 = TestCase (assertEqual "('g', 1, 'k') should be a clique"
                        ([('g', 1, 'h')] `elem` c) True)

cliquesTest3 = TestCase (assertEqual "Rest of graph should be a clique"
                        ([('a', 1, 'b'), ('b', 1, 'd'), ('a', 1, 'c'), ('c', 1, 'e'), ('e', 1, 'd'), ('a', 1, 'f')] `elem` c) True)


tests = TestList [TestLabel "hasbt Test 1" hasbtTest1,
                  TestLabel "hasbt Test 2" hasbtTest2,
                  TestLabel "hasbt Test 3" hasbtTest3,
                  TestLabel "equalbt Test 1" equalbtTest1,
                  TestLabel "equalbt Test 2" equalbtTest2,
                  TestLabel "reflectbt Test 1" reflecttbtTest1,
                  TestLabel "fringebt Test 1" fringebtTest1,
                  TestLabel "fringebt Test 2" fringebtTest2,
                  TestLabel "fullbt Test 1" fullbtTest1,
                  TestLabel "fullbt Test 2" fullbtTest2,
                  TestLabel "fullbt Test 3" fullbtTest3,
                  TestLabel "hasbtf Test 1" hasbtfTest1,
                  TestLabel "hasbtf Test 2" hasbtfTest2,
                  TestLabel "hasbtf Test 3" hasbtfTest3,
                  TestLabel "reflectbtf Test 1" reflecttbtfTest1,
                  TestLabel "fringebtf Test 1" fringebtfTest1,
                  TestLabel "fringebtf Test 2" fringebtfTest2,
                  TestLabel "fullbtf Test 1" fullbtfTest1,
                  TestLabel "fullbtf Test 2" fullbtfTest2,
                  TestLabel "fullbtf Test 3" fullbtfTest3,
                  TestLabel "insert Test 1" insertTest1,
                  TestLabel "insert Test 2" insertTest2,
                  TestLabel "insert Test 3" insertTest3,
                  TestLabel "has Test 1" hasTest1,
                  TestLabel "has Test 2" hasTest2,
                  TestLabel "remove Test 1" removeTest1,
                  TestLabel "remove Test 2" removeTest2,
                  TestLabel "remove Test 3" removeTest3,
                  TestLabel "flatten Test 1" flattenTest1,
                  TestLabel "findAllPaths Test 1" findAllPathsTest1,
                  TestLabel "findAllPaths Test 2" findAllPathsTest2,
                  TestLabel "findAllPaths Test 3" findAllPathsTest3,
                  TestLabel "findAllPaths Test 4" findAllPathsTest4,
                  TestLabel "reachable Test 1" reachableTest1,
                  TestLabel "reachable Test 2" reachableTest2,
                  TestLabel "reachable Test 3" reachableTest3,
                  TestLabel "reachable Test 4" reachableTest4,
                  TestLabel "minCostPath Test 1" minCostPathTest1,
                  TestLabel "minCostPath Test 2" minCostPathTest2,
                  TestLabel "cliques Test 1" cliquesTest1,
                  TestLabel "cliques Test 2" cliquesTest2,
                  TestLabel "cliques Test 3" cliquesTest3]

run = runTestTT tests
main = run
