module stdlib_experimental_system
use, intrinsic :: iso_c_binding, only : c_int, c_long
implicit none
private
public :: sleep

interface
#ifdef _WIN32
subroutine winsleep(dwMilliseconds) bind (C, name='Sleep')
!! void Sleep(DWORD dwMilliseconds)
!! https://docs.microsoft.com/en-us/windows/win32/api/synchapi/nf-synchapi-sleep
import c_long
integer(c_long), value, intent(in) :: dwMilliseconds
end subroutine winsleep
#else
integer(c_int) function usleep(usec) bind (C)
!! int usleep(useconds_t usec);
!! https://linux.die.net/man/3/usleep
import c_int
integer(c_int), value, intent(in) :: usec
end function usleep
#endif
end interface