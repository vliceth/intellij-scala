package org.jetbrains.plugins.scala.meta.trees

abstract class Converter extends TreeAdapter
                    with TypeAdapter
                    with Namer
                    with SymbolTable
                    with Attributes
                    with Utils
