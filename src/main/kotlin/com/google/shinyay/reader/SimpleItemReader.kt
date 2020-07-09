package com.google.shinyay.reader

import org.springframework.batch.item.ItemReader

class SimpleItemReader(private val reader: ItemReader<Int>): ItemReader<Int> {
    override fun read(): Int? = reader.read()
}