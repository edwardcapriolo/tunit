package io.teknek.tunit;

public interface CompareFunction<Returns, Sends> {
  Returns function(Sends x, Sends y);
}
