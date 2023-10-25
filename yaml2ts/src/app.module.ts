import {Module} from "@nestjs/common";
import {GenerateCommand} from "./cli";

@Module({
  providers: [GenerateCommand],
})
export class AppModule {}