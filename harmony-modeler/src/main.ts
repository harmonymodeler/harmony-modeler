import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';

import { Repository } from './models/repository.generated';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  await app.listen(3000);
  const repo: Repository = null;
}
bootstrap();
