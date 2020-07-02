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