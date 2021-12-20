cmake_minimum_required(VERSION 3.20)
project(scc0211 C CXX)

set(CMAKE_C_STANDARD 18)
set(CMAKE_CXX_STANDARD 11)

if (MSVC)
    add_compile_options(/W4 /WX)
else()
    add_compile_options(-Wall -Wextra -pedantic -Werror)
endif()

add_executable(a5e2  src/bin/a5e2.cxx)
add_executable(a6e1  src/bin/a6e1.cxx)
add_executable(a6e3  src/bin/a6e3.cxx)
add_executable(e2q4  src/bin/e2q4.cxx)

