import {compileFromFile} from 'json-schema-to-typescript'

const generate = async (inputPath) => {
  return await compileFromFile(inputPath)
}
export default {
  generate
}