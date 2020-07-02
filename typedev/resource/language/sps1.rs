("init", Some(sub_m)) => {
    let shell_name = sub_m.value_of("shell").expect("Shell name missing.");
    if sub_m.is_present("print_full_init") {
        init::init_main(shell_name).expect("can't init_main");
    } else {
        init::init_stub(shell_name).expect("can't init_stub");
    }
}