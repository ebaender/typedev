func main() {
	rootCmd.Execute()
}

func run(cmd *cobra.Command) {
	if horizonBase == "" || horizonTest == "" {
		log.Error("--base and --test params are required")
		cmd.Help()
		os.Exit(1)
	}