("config", Some(sub_m)) => {
    if let Some(name) = sub_m.value_of("name") {
        if let Some(value) = sub_m.value_of("value") {
            configure::update_configuration(name, value)
        }
    } else {
        configure::edit_configuration()
    }
}