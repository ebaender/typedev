("init", Some(sub_m)) => {
    let shell_name = sub_m.value_of("shell").expect("Shell name missing.");
    if sub_m.is_present("print_full_init") {
        init::init_main(shell_name).expect("can't init_main");
    } else {
        init::init_stub(shell_name).expect("can't init_stub");
    }
}
("prompt", Some(sub_m)) => print::prompt(sub_m.clone()),
("module", Some(sub_m)) => {
    if sub_m.is_present("list") {
        println!("Supported modules list");
        println!("----------------------");
        for modules in ALL_MODULES {
            println!("{}", modules);
        }
    }
    if let Some(module_name) = sub_m.value_of("name") {
        print::module(module_name, sub_m.clone());
    }
}
("config", Some(sub_m)) => {
    if let Some(name) = sub_m.value_of("name") {
        if let Some(value) = sub_m.value_of("value") {
            configure::update_configuration(name, value)
        }
    } else {
        configure::edit_configuration()
    }
}