import { Command, CommandRunner, Option } from 'nest-commander'
import { generate, TYPESCRIPT_GENERATORS } from '../generation'
import { ConsoleLogger } from '@nestjs/common'

export interface GenerateCommandOptions {
  inputPath?: string
  outputPath?: string
  debug?: boolean
  generator?: TYPESCRIPT_GENERATORS
}

const logger = new ConsoleLogger()

const defaultOptions: GenerateCommandOptions = {
  generator: TYPESCRIPT_GENERATORS.jsonSchemaToTypeScript,
}

@Command({
  name: 'gen',
  description: 'Generates typescript files',
})
export class GenerateCommand extends CommandRunner {
  constructor() {
    super()
  }

  async run(
    passedParam: string[],
    options?: GenerateCommandOptions,
  ): Promise<void> {
    if (options.debug) logger.log('Generating types from schemas')
    return generate({ ...defaultOptions, ...options })
      .catch((reason) => console.error(reason))
      .then(() => logger.log('Generation complete'))
  }

  @Option({
    flags: '-i, --inputPath [path]',
    description: 'Input path where yaml schemas are located',
  })
  parseInput(val: string): string {
    return val
  }

  @Option({
    flags: '-o, --outputPath [path]',
    description: 'Output path to generate typescript to',
  })
  parseOutput(val: string): string {
    return val
  }

  @Option({
    flags: '-g, --generator [generator]',
    description: 'Output path to generate typescript to',
  })
  parseGenerator(val: string): string {
    return TYPESCRIPT_GENERATORS[val]
  }

  @Option({
    flags: '-d, --debug',
    description: 'Enable debug logs',
  })
  parseDebug(value: string): boolean {
    return JSON.parse(value)
  }
}
