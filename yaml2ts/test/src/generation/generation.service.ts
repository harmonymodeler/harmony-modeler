import { ConsoleLogger } from '@nestjs/common';


import { promises as fs } from 'fs'
import {join, dirname, basename} from 'path';
import {load} from 'js-yaml'

import {GenerateCommandOptions} from '../cli';

import { default as jsonSchemaToTypeScript } from './jsonschema-to-typescript';

const logger = new ConsoleLogger()

export enum TYPESCRIPT_GENERATORS {
  'quicktype' = 'quicktype',
  'jsonSchemaToTypeScript' = 'jsonSchemaToTypeScript'
}

export const generators = {}
generators[TYPESCRIPT_GENERATORS.jsonSchemaToTypeScript] = jsonSchemaToTypeScript

export const generate = async (options: GenerateCommandOptions) => {
  const tempDir = await fs.mkdtemp('/tmp/webpack-gen')
  logger.debug(`json schema tmp dir: ${tempDir}`)
  logger.debug(`Using generator ${options.generator} of ${Object.keys(generators)}`)

  const inputFiles = await fs.readdir(options.inputPath)
  const promises = inputFiles.map(async (fileName) => {
    const filePath = join(options.inputPath, fileName)
    const fileContent = await fs.readFile(filePath, 'utf-8')
    const jsonSchema = load(fileContent)
    const jsonSchemaPath = join(tempDir, filePath.replace('.yaml', '.generated.json'))
    logger.debug(`Generated ${jsonSchemaPath}`)
    await fs.mkdir(dirname(jsonSchemaPath), {recursive: true})
    await fs.writeFile(jsonSchemaPath, JSON.stringify(jsonSchema))

    const outputFilePath = join(options.outputPath, basename(filePath.replace('.yaml', '.generated.ts')))
    const typeScript = await generators[options.generator].generate(jsonSchemaPath)
    await fs.writeFile(outputFilePath, typeScript, { flag: 'w+'})
    logger.debug(`Generated ${outputFilePath}`)
    return outputFilePath
  })

  return await Promise.all(promises)
    .then((result) => logger.log('Generation complete', result))
}