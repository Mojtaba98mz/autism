import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'jalali-moment';
import * as dayjs from 'dayjs';

@Pipe({
  name: 'jalali',
})
export class JalaliPipe implements PipeTransform {
  transform(value: dayjs.Dayjs | null | undefined): any {
    const date = value ? value.format('YYYY MM DD') : '';
    return moment(date, 'YYYY MM DD').locale('fa').format('YYYY/M/D');
  }
}
