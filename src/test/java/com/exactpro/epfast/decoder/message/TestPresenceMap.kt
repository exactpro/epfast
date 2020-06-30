package com.exactpro.epfast.decoder.message

import com.exactpro.epfast.template.dsl.template

class TestPresenceMap {
    private val templates = listOf(
        template("first template") {
            typeRef {
                name = "fooBar"
            }
            auxiliaryId = "id_1"
            instructions {
                int32("int32_1") {
                    optional = false
                    constant {
                        initialValue = "123"
                    }
                }
                int32("int32_null_1") {
                    optional = true
                    copy {
                        initialValue = "456"
                    }
                }
                templateRef {
                    name = "second template"
                }
                asciiString("ascii_1") {
                    optional = false
                }
                asciiString("ascii_null_1") {
                    optional = true
                    default {
                        initialValue = "QWERTY"
                    }
                }
            }
        },
        template("second template") {
            auxiliaryId = "id_2"
            instructions {
                int32("int32_2") {
                    optional = false
                    delta {
                        initialValue = "0"
                    }
                }
                int32("int32_null_2") {
                    optional = true
                    delta {
                        initialValue = "80"
                    }
                }
                asciiString("ascii_2") {
                    optional = false
                    tail {
                        initialValue = "DEF"
                    }
                }
                group("group") {
                    optional = false
                    instructions {
                        int32("int32_3") {
                            optional = false
                            default {
                                initialValue = "45"
                            }
                        }
                        int32("int32_null_3") {
                            optional = true
                            increment {
                                initialValue = "12"
                            }
                        }
                        asciiString("ascii_3") {
                            optional = false
                            constant {
                                initialValue = "AAA"
                            }
                        }
                        asciiString("ascii_null_3") {
                            optional = true
                            constant {
                                initialValue = "BBB"
                            }
                        }
                    }
                }
                sequence("sequence") {
                    optional = false
                    length {
                        name = "length"
                    }
                    instructions {
                        int32("int32_3") {
                            optional = false
                        }
                        int32("int32_null_3") {
                            optional = true
                        }
                        asciiString("ascii_3") {
                            optional = false
                        }
                        asciiString("ascii_null_3") {
                            optional = true
                        }
                    }
                }
                asciiString("ascii_null_2") {
                    optional = true
                    copy {
                        initialValue = "CCC"
                    }
                }
            }
        }
    )
}
