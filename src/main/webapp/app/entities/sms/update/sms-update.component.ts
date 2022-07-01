import { Component, ElementRef, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subject } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';

import { SmsService } from '../service/sms.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IGiver } from 'app/entities/giver/giver.model';
import { GiverService } from 'app/entities/giver/service/giver.service';
import { GiverModalService } from '../../../core/giver-selection/giver-modal.service';
import { GiverSelectionService } from '../../../core/giver-selection/giver-selection.service';
import * as moment from 'jalali-moment';
import { IProvince } from '../../province/province.model';
import { ICity } from '../../city/city.model';
import { ProvinceService } from '../../province/service/province.service';
import { CityService } from '../../city/service/city.service';

@Component({
  selector: 'jhi-sms-update',
  templateUrl: './sms-update.component.html',
})
export class SmsUpdateComponent {
  constructor(protected smsService: SmsService) {}

  content = '';

  send(content: string): void {
    this.smsService.sendMessage(content);
  }
}
