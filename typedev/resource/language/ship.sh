main() {
  for test in $TESTS; do
    header "Run $test..."
    ( eval zsh $test 2>&1 )
    local exitcode="$?"
    [ "$exitcode" != "0" ] && EXIT_CODE=$exitcode
  done