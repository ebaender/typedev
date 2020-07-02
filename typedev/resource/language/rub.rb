require 'generator/implementation'

module Generator
  # Immutable value object for storing paths
  class Paths
    attr_reader :track, :metadata
    def initialize(track:, metadata:)
      @track = track
      @metadata = metadata
    end
  end