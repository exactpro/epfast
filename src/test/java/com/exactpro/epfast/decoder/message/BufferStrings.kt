/******************************************************************************
 * Copyright 2020 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.exactpro.epfast.decoder.message

object BufferStrings {
    private const val firstT_part1 = "39 45 a3 81 " // 942755, 0
    private const val secondT_part1 = "39 45 a3 81 00 00 80 " // 942755, 0, "\u0000\u0000"
    private const val thirdT = "39 45 a3 81 00 00 80 41 42 c3 " // 942755, 0, "\u0000\u0000", ABC
    private const val secondT_part2 = "41 42 c3 " // ABC
    private const val firstT_part2 = "00 00 80 41 42 c3 " // "\u0000\u0000", ABC
    private const val optionalSequenceLength = "84 "
    private const val mandatorySequenceLength = "83 "
    private const val nullLength = "80 "

    const val bytesString = firstT_part1 + secondT_part1 + thirdT + secondT_part2 + firstT_part2
    const val optionalSequenceBytesString = firstT_part1 + secondT_part1 + optionalSequenceLength + thirdT + thirdT + thirdT + secondT_part2 + firstT_part2
    const val mandatorySequenceBytesString = firstT_part1 + secondT_part1 + mandatorySequenceLength + thirdT + thirdT + thirdT + secondT_part2 + firstT_part2
    const val nestedGroupBytesString = firstT_part1 + secondT_part1 + firstT_part1 + thirdT + firstT_part2 + secondT_part2 + firstT_part2
    const val nullSequenceBytesString = firstT_part1 + secondT_part1 + nullLength + secondT_part2 + firstT_part2
}
