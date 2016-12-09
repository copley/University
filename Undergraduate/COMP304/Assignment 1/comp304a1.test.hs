import Test.HUnit
import qualified COMP304A1 as A1

countTest1 = TestCase ( assertEqual "Count should be 4 for 1 in [1,1,1,1]"
                        (A1.count 1 [1,1,1,1]) 4)

countTest2 = TestCase ( assertEqual "Count should be 0 for 1 in [2,3,4,5]"
                        (A1.count 1 [2,3,4,5]) 0)

countTest3 = TestCase ( assertEqual "Count should be 3 for 1 in [2,1,3,4,1,2,1]"
                        (A1.count 1 [2,1,3,4,1,2,1]) 3)

countTest4 = TestCase ( assertEqual "Count should be 0 in empty array"
                        (A1.count 1 []) 0)


allPosTest1 = TestCase ( assertEqual "Positions should be [0,1,2] for 1 in [1,1,1]"
                         (A1.allPos 1 [1,1,1]) [0,1,2])

allPosTest2 = TestCase ( assertEqual "Positions should be [] for 1 in [2,3,4,5]"
                         (A1.allPos 1 [2,3,4,5]) [])

allPosTest3 = TestCase ( assertEqual "Positions should be [1,3,4] for 1 in [2,1,4,1,1]"
                         (A1.allPos 1 [2,1,4,1,1]) [1,3,4])

allPosTest4 = TestCase ( assertEqual "Positions should be [] for 1 in []"
                         (A1.allPos 1 []) [])


firstLastPosTest1 = TestCase ( assertEqual "Positions should be (1,3) for 1 in [1,1,1]"
                         (A1.firstLastPos 1 [1,1,1]) (1,3))

firstLastPosTest2 = TestCase ( assertEqual "Positions should be (0,0) for 1 in [2,3,4,5]"
                         (A1.firstLastPos 1 [2,3,4,5]) (0,0))

firstLastPosTest3 = TestCase ( assertEqual "Positions should be (2,4) for 1 in [2,1,4,1,5]"
                         (A1.firstLastPos 1 [2,1,4,1,1,2,2,2]) (2,5))

firstLastPosTest4 = TestCase ( assertEqual "Positions should be (0,0) for 1 in []"
                               (A1.firstLastPos 1 []) (0,0))


sort1Test1 = TestCase ( assertEqual "List not sorted correctly"
                        (A1.sort1 [100,99..1]) [1..100])

sort2Test1 = TestCase ( assertEqual "List not sorted correctly"
                        (A1.sort2 [100,99..1]) [1..100])

treeSortTest1 = TestCase ( assertEqual "List not sorted correctly"
                        (A1.treeSort [100,99..1]) [1..100])


map1 = [(1, 1)]
hasKeyTest1 = TestCase ( assertEqual "Key is in map should be true"
                         (A1.hasKey 1 map1) True)

hasKeyTest2 = TestCase ( assertEqual "Key is not in map should be false"
                         (A1.hasKey 2 map1) False)

getKeyTest1 = TestCase ( assertEqual "Value should be 1 for key 1"
                       (A1.getVal 1 map1) 1)

map2 = A1.setVal 2 2 map1
setKeyTest1 = TestCase ( assertEqual "Key was added to map should be true"
                         (A1.hasKey 2 map2) True)

map3 = A1.setVal 1 2 map2
setKeyTest2 = TestCase ( assertEqual "Key was updated"
                         (A1.getVal 1 map3) 2)

map4 = A1.delKey 1 map2
delKeyTest1 = TestCase ( assertEqual "Key should be deleted"
                         (A1.hasKey 1 map4) False)


preMap1 = [1,1,2,2,1,1,3,3,4]
map5 = A1.buildMap preMap1

buildMapTest1 = TestCase ( assertEqual "1 occored 4 times"
                           (A1.getVal 1 map5) 4)

buildMapTest2 = TestCase ( assertEqual "2 occored 2 times"
                           (A1.getVal 2 map5) 2)

buildMapTest3 = TestCase ( assertEqual "3 occored 2 times"
                           (A1.getVal 3 map5) 2)

buildMapTest4 = TestCase ( assertEqual "4 occored 1 times"
                           (A1.getVal 4 map5) 1)

tests = TestList [TestLabel "Count Test 1" countTest1,
                  TestLabel "Count Test 2" countTest2,
                  TestLabel "Count Test 3" countTest3,
                  TestLabel "Count Test 4" countTest4,
                  TestLabel "All Pos Test 1" allPosTest1,
                  TestLabel "All Pos Test 2" allPosTest2,
                  TestLabel "All Pos Test 3" allPosTest3,
                  TestLabel "All Pos Test 4" allPosTest4,
                  TestLabel "First Last Pos Test 1" firstLastPosTest1,
                  TestLabel "First Last Pos Test 2" firstLastPosTest2,
                  TestLabel "First Last Pos Test 3" firstLastPosTest3,
                  TestLabel "Sort1 Test 1" sort1Test1,
                  TestLabel "Sort2 Test 1" sort2Test1,
                  TestLabel "Tree Sort Test 1" treeSortTest1,
                  TestLabel "Has Key Test 1" hasKeyTest1,
                  TestLabel "Has Key Test 2" hasKeyTest2,
                  TestLabel "Get Key Test 1" getKeyTest1,
                  TestLabel "Set Key Test 1" setKeyTest1,
                  TestLabel "Set Key Test 2" setKeyTest2,
                  TestLabel "Delete Key Test 1" delKeyTest1,
                  TestLabel "Build Map Test 1" buildMapTest1,
                  TestLabel "Build Map Test 2" buildMapTest2,
                  TestLabel "Build Map Test 3" buildMapTest3,
                  TestLabel "Build Map Test 4" buildMapTest4]

run = runTestTT tests
main = run
