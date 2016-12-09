# cp comp304a2.lhs COMP304A2.lhs
{ echo "> module COMP304A2 where"; echo ""; cat comp304a2.lhs; } >COMP304A2.lhs
runhaskell comp304a2.test.hs
